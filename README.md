# 📱 Xskillent – Skill Sharing & Learning App

**Xskillent** is a mobile application that connects people who want to learn skills with those who are ready to share them. Users can register as either a **Skill Learner** or a **Skill Sharer**, and interact via chat, short videos, and 1-on-1 video sessions. The app also supports paid sessions, community groups, and in-app courses.

---

## 🚀 Features

### 👥 User Roles
- **Skill Sharer**
  - Create a portfolio/profile
  - Upload short skill videos to a feed
  - Host live 1-on-1 video sessions
  - Use a dynamic calendar to set available slots
  - Offer paid or free sessions
  - Award certificates to learners
  - Offer system-provided courses

- **Skill Learner**
  - Create a profile with skill interests
  - Filter sharers by city for offline interaction
  - Connect with sharers via chat
  - Book available slots
  - Track learning progress and received materials

---

## 📚 System Features
- **Short Video Feed** – For promoting skills and attracting learners
- **Live Video Meetings** – Via Jitsi SDK
- **Real-Time Chat** – Using Firebase Realtime DB or Firestore
- **Certificate Sharing** – Auto-generated certificates post learning
- **In-App Courses** – System-curated content for common skills
- **Community Formation** – Join or create interest-based groups
- **Sharer Verification** – Based on uploaded certificates
- **Contests & Top Creators** – Highlight high-engagement sharers
- **Notifications** – Via Firebase Cloud Messaging (FCM)
- **In-App Payments** – Optional paid sessions via Razorpay

---

## 🔧 Tech Stack

- **Frontend**: Android Studio (XML + Kotlin)
- **Backend**: Firebase (Firestore, Storage, Realtime DB, FCM), Kotlin (app-side logic)
- **Authentication**: Firebase Auth
- **Media & Calls**: Firebase Storage, Jitsi SDK, ExoPlayer
- **Payments**: Razorpay SDK
- **Notifications**: Firebase Cloud Messaging (FCM)

---

## 👨‍💻 Team Roles

| Name      | Responsibility                          |
|-----------|------------------------------------------|
| Harshil   | UI/UX Design, Android Frontend, Docs     |
| Femil     | Android Frontend + Firebase Integration  |
| Hitarth   | Android Frontend + Backend Logic         |
| Hiren     | Database Design, Testing, Firebase Rules |

---

## 📱 Permissions Used

- 📷 Camera – For video uploads and calls  
- 🎤 Microphone – For video meetings  
- 💾 Storage – For accessing learning materials and uploads  
- 🌐 Internet – Required for Firebase operations and media  

---

## 📂 Folder Structure

```plaintext
Xskillent/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/xskillent/    # Kotlin source files
│   │   │   ├── res/                  # XML layouts, drawables
│   │   │   └── AndroidManifest.xml
│
├── README.md
└── .gitignore
```

🛠️ Setup Instructions
Clone this repo:
git clone https://github.com/<your-team>/Xskillent.git

Open in Android Studio

Add your own google-services.json (Firebase setup)

Add Razorpay API Key to strings.xml

Build & Run on emulator or device

