# UserRepositoryPractice
Чисто с целью практики написал свой спринг-подобный пользовательский репозиторий

![Макет взаимодействий](images/prac_table.svg)

Firstly we create context and load there necessary classes (e.g. Repository and UserService)
Then we create UserService object just "retrieving" it from context, because
ApplicationContext had already created repository and injected it to freshly-created
user service. Belold, we can control users through methods!
