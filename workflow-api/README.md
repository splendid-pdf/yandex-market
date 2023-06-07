# workflow-api

Данный сервис предназначен для распространения изменений в системе.
Работа с сервисом происходит через рест (с методом http - POST).

Основная единица над которой произоводится работы - объект типа Operation, который содержит в себе всю информацию, которую нужно обработать или распространить в другие сервисы.

При получении запроса по ресту, сервис обращается к указанной Schema для последовательной обработке операции (шаг за шагом).

Рассмотрим простой пример. Нам необходимо удалить продукты и их фотографии по переданным идентификаторам.
Опишем модель ProductGroupOperation.
```
public class ProductGroupOperation extends Operation {
    List<String> product_ids;
    List<String> photo_ids;
}
```
Имея операцию, необходимо описать шаги, которые последовательно обработают данную операцию.

```
@RequiredArgsConstructor
public class ProductGroupDeleteStep extends AbstractStep<ProductGroupOperation> {
    private final ProductGateway gateway;

    @Override
    public StepResult<ProductGroupOperation> apply() {
        Response response = gateway.deleteProductGroup(this.operation.getProductIds());
        if (response.is2xxSuccessfull()) {
            return StepResult.ok(this.operation(), this.operationName());
        }
        
        return StepResult.failed(this.operation(), this.operationName());
    }
}
```



```
@RequiredArgsConstructor
public class PhotoGroupDeleteStep extends AbstractStep<ProductGroupOperation> {
    private final UploadGateway gateway;

    @Override
    public StepResult<ProductGroupOperation> apply() {
        Response response = gateway.deletePhotoGroup(this.operation.getPhotoIds());
        if (response.is2xxSuccessfull()) {
            return StepResult.ok(this.operation(), this.operationName());
        }
        
        return StepResult.failed(this.operation(), this.operationName());
    }
}
```


Далее, имея шаги, описывается схема, в которой шаги указываются в порядке, в котором они будут вызваны.
```
@Component
public class DeleteProductGroupScheme implements SchemaProvider<ProdictGroupOperation> {

    public Schema<ProdictGroupOperation> provideSchema(ProdictGroupOperation operation) {
        return SchemaBuilder.builder(operation)
                .decorator(new LogDecorator<>(new TimeTrackDecorator<>()))
                .step(new ProductGroupDeleteStep(), "productGroupDeleteStep")
                .step(new PhotoGroupDeleteStep(), "photoGroupDeleteStep")
                .build();
    }
}
```

Конфигурируете Workflow
```
@Configuration
public class WorkflowConfiguration {

    @Bean
    public Workflow<ProdictGroupOperation> deleteProductGroup(DeleteProductGroupScheme schema) {
        return new Workflow<>(schema);
    }
}
```

И создаем эндпоинт для обработки операции по заданному workflow.
```
@RestController
public class WorkflowController {
    private final Workflow<ProdictGroupOperation> deleteProductGroupWorkflow;

    @PostMapping()
    public Operation deleteProductGroup(ProdictGroupOperation operation) {
        ProdictGroupOperation result =  deleteProductGroupWorkflow.process(operation);
        return result;
    }
}
```