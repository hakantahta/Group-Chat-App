Uygulamamda veri tabanı olarak Firebase Realtime Database kullandım. Firebase gerçek zamanlı veri işleme ve 
senkronizasyon özellikleriyle grup sohbet uygulaması için uygun bir çözüm sunuyor.

Uygulamada kullanıcı girişi Google ve Facebook ile oturum açma sistemi kullanılarak sağlanmaktadır. 
Firebase Auth ara yüzü sayesinde kolayca sosyal oturum açma entegrasyonu yapılabildi.

Gruplar ve sohbetlere ait veriler Firebase Realtime Database üzerinde tutulmaktadır. Veri tabanında gerçek zamanlı dinleme ve 
güncelleme özellikleri sayesinde sohbet verileri tüm kullanıcılara senkronize edilmektedir.

Uygulama üç ana ekrandan oluşmaktadır: Ana sayfa, grup listesi ve sohbet ekranı. Kullanıcı ilk önce ana sayfada giriş yapmakta, ardından grup listesine erişerek 
farklı gruplara katılabilmektedir. Son olarak seçtiği grupta diğer kullanıcılarla sohbet edebilmektedir.
