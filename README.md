## Получение данных с сервера, с помощью Retrofit. А также, хранение данных в базе данных[Sqlite], использование RxJava/RxAndroid и загрузка изображения Picasso.
## Обобщенно: Retrofit + RxJava/RxAndroid + Sqlite + Picasso + ListView + Fragment + Singleton[Design Patterns].

### Данные товаров косметики будут получены по API: http://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline&product_type=blush&price_greater_than=12&price_less_than=20
### Документация по API: http://makeup-api.herokuapp.com/

### Подробнее.

### 1] Главный экран, на котором есть форма для заполнения. Выбрать можно фирму, тип продукции и ценовой лимит. Нажав на кнопку "Поиск" - переходим на следующий экран и запрашиваем данные по API. Поля можно оставить пустыми или выбрать всю продукцию разом.
![project_makeup_form_ver'](https://user-images.githubusercontent.com/15383481/86041947-8a187080-ba57-11ea-9178-110b974624eb.png)
![project_makeup_form_hor'](https://user-images.githubusercontent.com/15383481/86041955-8c7aca80-ba57-11ea-9fbb-e26b0d407d58.png)

### 2] Экран, на котором отображены названия товаров и их изображения, взятые из базы данных. После получения данных по API - они автоматически добавляются в базу данных, а оттуда и на экран в виде списка. Список прокручиваемый. 
![project_makeup_list_ver'](https://user-images.githubusercontent.com/15383481/86042263-f4311580-ba57-11ea-9400-9d638a129e0b.png)
![project_makeup_list_hor'](https://user-images.githubusercontent.com/15383481/86042265-f5fad900-ba57-11ea-8499-5545bef21387.png)
### Удалить товар можно продолжительным нажатием на сам товар. Будет выведено диалоговое окно о подтверждении удаления. Товар будет удален из базы данных и из списка. 
### Можно очистить весь список товаров, удалив их из базы данных.
### Для получения подробной информации о товаре - нужно выбрать его из списка.

### 3] Экран, на котором можно получить подробную информацию о выбранном товаре. Выводится вся доступная информация.
![project_makeup_info_ver'](https://user-images.githubusercontent.com/15383481/86042346-13c83e00-ba58-11ea-9ebe-af75a1e611a6.png)
![project_makeup_info_hor'](https://user-images.githubusercontent.com/15383481/86042349-15920180-ba58-11ea-9603-6fa0281bc516.png)
### Можно сразу удалить товар. Товар будет удален из базы данных и из списка. Будет выведено диалоговое окно о подтверждении удаления.
### Можно обновить информацию о товаре, перейдя на экран с "редактором".

### 4] Экран, на котором можно обновить информацию о товаре. Нужно заполнить все поля, после чего будет сделан запрос в базу данных для обновления информации. 
![project_makeup_update_ver'](https://user-images.githubusercontent.com/15383481/86042452-39554780-ba58-11ea-9a99-2b2b56b0567d.png)
![project_makeup_update_hor'](https://user-images.githubusercontent.com/15383481/86042459-3c503800-ba58-11ea-94a3-489a04cc0dc5.png)

### Примечание:

#### 1] Это лишь пример использования Retrofit/RxJava/RxAndroid/Sqlite/Picasso/прочее в одной связке друг с другом. 
#### 2] Почти все окна содержат фрагменты.
#### 3] Для облегчения подстройки под разные разрешения экрана, были использованы единицы sdp/ssp.

#### sdp: https://github.com/intuit/sdp
#### ssp: https://github.com/intuit/ssp 

### Device: Samsung galaxy note 5.
