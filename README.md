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
