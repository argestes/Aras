# Aras Görev Yönetim Uygulaması

## Proje Açıklaması

Bu proje Aras Digital için geliştirilmiş basit bir demo to-do uygulamasıdır. Uygulama Jetpack Compose, Room, Kotlin Coroutines kütüphanelerinin birlikte kullanımını gösterir. Genel olarak MVVM mimarisi izlenmiştir.

## Özellikler

- Görev ekleme, düzenleme ve silme
- Görevleri tamamlandı olarak işaretleme
- Görevleri tümü, aktif ve tamamlanmış olarak filtreleme
- Açık ve koyu tema desteği
- Türkçe ve İngilizce dil desteği
- Kaydedilmemiş değişiklikler için uyarı

## Dosya Yapısı İncelemesi

Aşağıda projenin temel dosya ve dizin yapısı hakkında genel bir bakış sunulmuştur:

- `ui/`: Jetpack Compose bileşenlerini ve temalarını içerir.
- `presentation/`: ViewModel’ler ve UI mantığını içerir.
- `domain/`: Veri modelleri ve repository arayüzlerini içerir.
- `data/`: Room Entity, DAO ve repository implementasyonlarını içerir.
- `di/`: Hilt modülleri ve bağımlılık sağlayıcılarını içerir.
- `MainActivity.kt`: Uygulamanın giriş noktasıdır.

## Kullanılan Teknolojiler ve Kütüphaneler

- **Kotlin**: Ana programlama dili.
- **Jetpack Compose**: Modern, bildirimsel (declarative) kullanıcı arayüzü oluşturmak için kullanıldı.
- **Hilt**: Bağımlılık enjeksiyonu (dependency injection) için tercih edildi.
- **Jetpack Navigation**: Uygulama içi ekran geçişlerini yönetmek için kullanıldı.
- **Room**: Yerel veritabanı işlemleri ve veri kalıcılığı için entegre edildi.
- **StateFlow & Coroutines**: Asenkron işlemlerin ve durum (state) yönetiminin verimli bir şekilde yapılması için kullanıldı.
- **Google Truth**: Testlerde daha okunaklı ve anlaşılır iddialar (assertions) yazmak için kullanıldı.

## Derleme ve Çalıştırma Adımları

Projeyi yerel makinenizde derlemek ve çalıştırmak için aşağıdaki adımları izleyebilirsiniz:

1.  **Projeyi Klonlayın**:
    ```bash
    git clone https://github.com/argestes/Aras.git
    ```

2.  **Android Studio'da Açın**:
    - Android Studio'yu açın.
    - `Open an Existing Project` seçeneğini kullanarak klonladığınız proje dizinini seçin.

3.  **Bağımlılıkları Yükleyin**:
    - Android Studio, projeyi açtığınızda gerekli olan Gradle bağımlılıklarını otomatik olarak senkronize edecektir.

4.  **Uygulamayı Çalıştırın**:
    - Bir emülatör veya fiziksel bir Android cihaz bağlayın.
    - Android Studio'daki `Run 'app'` (▶) butonuna tıklayarak uygulamayı başlatın.

## Özel Notlar ve Ek Geliştirmeler

- **Dil Desteği**: Uygulama şu anda Türkçe (varsayılan) ve İngilizce dillerini desteklemektedir. Yeni diller `res/values-<dil_kodu>/strings.xml` yapısı takip edilerek kolayca eklenebilir.
- **Tema**: Sistem, açık ve koyu tema seçenekleri mevcuttur. Tema renkleri `ui/theme/Color.kt` dosyasından yönetilmektedir.
- **Gelecek Geliştirmeler**: Gelecekte, görevlere hatırlatıcı ekleme, görevleri kategorilere ayırma veya bulut senkronizasyonu gibi özellikler eklenebilir. Veritabanı işlemleri veya diğer hatalar için daha kapsamlı bir hata yönetimi (error handling) mekanizması kurulabilir.
