# workflow-api

Cервис предназначен для распространения изменений в системе при работе с аггрегатами.
<br>Работа с сервисом происходит через рест (с методом http - POST).
<br>При получении запроса сервис обращается к указанной Schema для последовательной обработке операции (шаг за шагом).

<b>Обзор основных компонент:</b>

* Операция - объект типа Operation, над которым производится работа.
* Шаг - объект типа AbstractStep, базовая единица работы манипулирующая объектом Operation.
* Схема - объект типа Schema, в котором задается последовательность шагов.


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
    public StepResult<ProductGroupOperation> apply(O o) {
        Response response = gateway.deleteProductGroup(this.operation.getProductIds());
        if (response.is2xxSuccessfull()) {
            return StepResult.ok(o, this.stepName());
        }
        
        return StepResult.failed(o, this.stepName());
    }
}
```



```
@RequiredArgsConstructor
public class PhotoGroupDeleteStep extends AbstractStep<ProductGroupOperation> {
    private final UploadGateway gateway;

    @Override
    public StepResult<ProductGroupOperation> apply(O o) {
        Response response = gateway.deletePhotoGroup(this.operation.getPhotoIds());
        if (response.is2xxSuccessfull()) {
            return StepResult.ok(o, this.stepName());
        }
        
        return StepResult.failed(o, this.stepName());
    }
}
```


Далее, имея шаги, описывается схема, в которой шаги указываются в порядке, в котором они будут вызваны.
Тут же мы указываем, что ко всем шагам должна быть применена цепочка декораторов.
Обьект типа BaseDecoratorChain содержит в себе 3 декоратара: 
* первый ловит исключения.
* второй измеряет время выполнения шага в наносекундах.
* третий логирует начало и завершение работы шага.<br>

<b><i>Пользователь может определить cвои декораторы и собрать из них свою цепочку декораторов</i></b>
```
@Component
public class DeleteProductGroupScheme implements SchemaProvider<ProdictGroupOperation> {

    public Schema<ProdictGroupOperation> provideSchema(ProdictGroupOperation operation) {
        return SchemaBuilder.builder(operation)
                .withDecoratorChain(new BaseDecoratorChain())               // Опционально
                .withRetry(Retry.create().maximumAttempts(3))               // Опционально
                .step(new ProductGroupDeleteStep()                          // Можно описать шаг таким образом
                            .fallback(() -> YOUR_FALLBACK)
                            .operationName("productGroupDeleteStep")
                )
                .step(new PhotoGroupDeleteStep(), "photoGroupDeleteStep")   // Можно и так
                .build();
    }
}
```

В конфигурации создаем бин типа Workflow, который принимает в конструктор схему и при вызове этого бина управление
передается схеме.
```
@Configuration
public class WorkflowConfiguration {

    @Bean
    public Workflow<ProdictGroupOperation> deleteProductGroup(DeleteProductGroupScheme schema) {
        return new Workflow<>(schema);
    }
}
```

Осталось прописать эндпоинт для обработки операции по заданному workflow.
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