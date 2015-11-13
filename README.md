# MAI 

Список используемых библиотек:
1. jdatepicker-1.3.4.jar
2. jdatepicker-1.3.4-javadoc.jar
3. jxl.jar (jexcelapi-2.6.12)
4. mysql-connector-java-5.1.36-bin.jar

Версия Java: 1.8.

версия MySql:
 mysql-5.6.25-winx64
 
 "логин, пароль к БД см. в ДБCreator-е"

Для начала работы с программой нужно:
1. Запустить сервер (START_SERVER.bat)
2. Создать новую БД (вспомогательная программа DatabaseCreator)
3. Ввести в БД начальные данные (вспомогательная программа ExcelReader 
		+ данные в таблицах Excel : general_data.xlsx, worker_default_values.xlsx
		 с названиями столбцов = названиям полей таблиц БД)
4. Запустить основную программу ZPCounter, ввести пароль.