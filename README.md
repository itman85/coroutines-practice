# Coroutines Practice
- Coroutine must have Coroutine Scope to run, such as GlobalScope, ViewModelScope
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

### các tình huống cần tìm hiểu
1. sự khác nhau giữa viewModelScope.launch(context = Dispatchers.Main) {} và viewModelScope.launch {} tại sao 2 bên block UI theo
các mức độ khác nhau; dùng code usecase 10 làm ví dụ
2. tại sao cùng là suspend function nhưng retrofit (cho timeout 30s luôn) hay room đều ko block UI khi run trong main context
nhưng heavy operation lại block ui, lấy code ví dụ usecase 10, 1