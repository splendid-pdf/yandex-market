# workflow-api

Данный сервис предназначен для распространения изменений в системе при работе с аггрегатами.
<br>Работа с сервисом происходит через рест (с методом http - POST).

Работа осуществляется над объектом типа Operation, который содержит в себе всю информацию, которую нужно распространить в другие сервисы.

При получении запроса по ресту, сервис обращается к указанной Schema для последовательной обработке операции (шаг за шагом).
<br>Шаг - объект типа AbstractStep, базовая единица работы манипулирующая объектом Operation. <br><br>
Каждый шаг может быть обогащен функционалом за счет использования DecoratorChain.
При использовании объекта DecoratorChain в схеме надо понимать, что цепочка декораторов будет применена к каждому шагу внутри одной схемы.  


Рассмотрим простой пример при работе с двумя аггрегатами. Нам необходимо удалить продукты и их фотографии по переданным идентификаторам.
<br>Опишем модель ProductGroupOperation.
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
Тут же мы указываем, что ко всем шагам должна быть применена цепокчка декораторов.
Обьект типа BaseDecoratorChain содержит в себе 3 декоратара: 
* один осуществляет try-catch на случай, если что то пойдет не так
* второй измеряет время выполнения шага в наносекундах
* третий логирует начало и завершения работы шага.<br>
<b><i>Пользователь может определить cвои декораторы и собрать из них свою цепочку декораторов<i><b>
```
@Component
public class DeleteProductGroupScheme implements SchemaProvider<ProdictGroupOperation> {

    public Schema<ProdictGroupOperation> provideSchema(ProdictGroupOperation operation) {
        return SchemaBuilder.builder(operation)
                .decoratorChain(new BaseDecoratorChain())
                .step(new ProductGroupDeleteStep(), "productGroupDeleteStep")
                .step(new PhotoGroupDeleteStep(), "photoGroupDeleteStep")
                .build();
    }
}
```

Пришло время создать бин типа Workflow, Workflow принимает в конструктор схему и при его вызове передает управление схеме.
```
@Configuration
public class WorkflowConfiguration {

    @Bean
    public Workflow<ProdictGroupOperation> deleteProductGroup(DeleteProductGroupScheme schema) {
        return new Workflow<>(schema);
    }
}
```

Осталось прописать эндпоинт для обработки Вашей операции по заданному workflow.
```
@RestController
@RequestMapping(....)
public class WorkflowController {
    private final Workflow<ProdictGroupOperation> deleteProductGroupWorkflow;

    @PostMapping(....)
    public ResponseEntity<OperationProgressReport> deleteProductGroup(ProdictGroupOperation operation) {
        return ResponseEntity.ok(deleteProductGroupWorkflow.process(operation));
    }
}
```