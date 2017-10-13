## Перевод проекта на https


### Перевод проекта на https можно разделить на три этапа:
1. Настройка проекта, как ресурса, до которого можно получить доступ по https;
2. Получение SSL сертификата;
3. Настройка Tomcat.
4. Настройка Heroku


### 1. 
Первый пункт осуществляется добавлением в файле spring-security.xml настроек доступа к страницам в виде requires-channel="https". Если дальше вы заливаете ваш проект на хост, поддерживающий https (например, heroku), то этого будет достаточно. Остальные шаги можно не смотреть.

### 2. 
Если вы запустите проект на вашей локальной машине, то, скорее всего, ничего не заработает, поскольку нужно настроит Tomcat с ключом.

а). Для домашних экспериментов можно обойтись локальным ключом. Вариант получения локального самоподписанного ключа следующий:
> For windows
- Идем в корень вашей java (%JAVA_HOME%) и в cmd запускаем команду
"%JAVA_HOME%\bin\keytool" -genkey -alias tomcat -keyalg RSA
- Создаем пароль (по умолчанию Tomcat используем "changeit"). В папку вашего аккаунта (C:\Users\{имя вашего аккаунта}) будет добавлен файл ".keystore".
- Переходите к пункту 3.а).

б). Для получения "серьёзного" ключа можно воспользоваться сервисом https://www.sslforfree.com/.
- Вводим интернет адрес вашего сайта + жмём "Create ...";
- Жмём "Manually Verify";
- Скачиваем безличный файл по ссылке "Download File #1";
- В вашем проекте topjava создаём папочку  .well-known/acme-challenge в папке webapp. Не забудьте эту папку отметить в файле spring-security.xml как <http pattern="/.well-known/**" security="none"/> !
- Заливаете на ваш хост;
- После чего жмём "Download SSL Certificate";
- Скачается архив с 3мя файлами: ca_bundle.crt, certificate.crt, private.key;
- Переходим к настройке Tomcat (п. 3.б)).


### 3. 
Настройки Tomcat можно посмотреть по адресу https://tomcat.apache.org/tomcat-8.5-doc/ssl-howto.html
Опишу ещё их здесь
а).
- В файл по адресу $CATALINA_BASE/conf/server.xml вставляем следующие строки (у меня $CATALINA_BASE это \JAVA\apache-tomcat-8.5.20)

         <Connector
           protocol="org.apache.coyote.http11.Http11NioProtocol"
           port="8443" maxThreads="200"
           scheme="https" secure="true" SSLEnabled="true"
           keystoreFile="$conf/.keystore" keystorePass="changeit"
           clientAuth="false" sslProtocol="TLS"/>

- В папку $CATALINA_BASE/conf копируем ключ .keystore, полученный в пункте 2.а);
- Всё. Можно запускать и работать. Правда chrome будет ругаться, что ключ самоподписанный, но для тестов сгодится.

б). - В файл по адресу $CATALINA_BASE/conf/server.xml вставляем следующие строки (у меня $CATALINA_BASE это \JAVA\apache-tomcat-8.5.20)

	<Connector
           protocol="org.apache.coyote.http11.Http11AprProtocol"
           port="8443" maxThreads="200"
           scheme="https" secure="true" SSLEnabled="true"
           SSLCertificateFile="/conf/certificate.crt"
           SSLCertificateKeyFile="/conf/private.key"
           SSLVerifyClient="optional" SSLProtocol="TLSv1+TLSv1.1+TLSv1.2"/>

- В папку $CATALINA_BASE/conf копируем ключ сертификат certificate.crt и ключ .private.key, полученный в пункте 2.б);
- Идем в папку $CATALINA_BASE/bin и через cmd запускаем Tomcat: catalina.bat jpda start.
- Смотрим на логи. Не обругал ли Tomcat наши ключи и сертификаты.
- Если всё в порядке, то наслаждаемся работай сайт.

- В принципе, это SSL можно использовать и для локального тестирования.

### 4. 
Для работы с heroku Вам необходимо докупить возможность работы с SSL. Об этом недвусмысленно намекает надпись "Upgrade to paid dynos to configure Heroku SSL" в настройках.
- Установите себе Heroku CLI (https://devcenter.heroku.com/articles/heroku-cli#download-and-install);
- Залогинтесь из cmd: heroku login:
- И залейте свои сертификаты, полученные в пункте 2: heroku certs:add ca_bundle.crt certificate.crt private.key --app {youapp}

