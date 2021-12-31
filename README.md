# Coroutines Characteristics
- Coroutine must have Coroutine Scope to run, such as GlobalScope, ViewModelScope
- Coroutine started in the same scope from a hierarchy
- A parent job (coroutine) won't complete until all of its children have completed
- Cancelling a parent will cancel all children, but cancelling a child won't cancel the parent or siblings
- If a child coroutine fails, the exception is propagated upwards and depending on the job type, either all siblings are cancelled or not
- Coroutine always go with Coroutine Context which define which context (Main thread or other thread) coroutine will run under
- Coroutine can include many coroutines
- Coroutine can include many suspend functions
## Coroutine builder
- launch {} non-blocking; return Job
- async {} non-blocking; return Deferred = Job with Result
- runBlocking {}  blocking code return value;

## Suspend functions
- suspend function: run in parent suspend function or in coroutine (launch, async, runBlocking); blocking code (wrap into launch or async to transform into non-blocking code)
- withContext() create suspend function with context (main or worker thread) will run under; return value
- suspend function run in main coroutine context will be a blocking code in Main thread

## Coroutine Scope (pre-defined)
- GlobalScope
- ViewModelScope
- LifecycleScope

### các tình huống cần tìm hiểu
1. sự khác nhau giữa viewModelScope.launch(context = Dispatchers.Main) {} và viewModelScope.launch {} tại sao 2 bên block UI theo
các mức độ khác nhau; dùng code usecase 10 làm ví dụ
2. tại sao cùng là suspend function nhưng retrofit (cho timeout 30s luôn) hay room đều ko block UI khi run trong main context
nhưng heavy operation lại block ui, lấy code ví dụ usecase 10, 1
3. Ask team: domain layer sai coroutine dc ko? scope dau the pass xuong domain -> data dc dung ko? vi domain la no android framework ma?
vay neu sai clean thi domain,data chi sai suspend function thoi nhi?
4. playground / exceptionhandling / 4_launch_and_async de moi nguoi thao luan vi sao async ko throw exception ma launch lai throw exception
5. playground/ exceptionhandling / 5_exception_handling_specifics_coroutineScope : coroutineScope duoc dung the nao? trong truong hop gi? tao sai this trong day la instance cua coroutine scope roi?
6. trong usecase13 tai sao supervisorScope != viewModelScope.launch(SupervisorJob()) , with supervisorScope thi no ko cancel sibling coroutine neu co 1 coroutine fail, nhung viewModelScope.launch(SupervisorJob()) van cancel du co SupervisorJob
so sanh voi 5_exception_propagation.kt thi thay co ve chua hieu lam SupervisorJob, tai sao co try catch roi ma fail coroutine van bi propagate len top level coroutine