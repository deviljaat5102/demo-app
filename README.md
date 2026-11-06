# AirWallet Demo

Android app and responsive admin panel inspired by the reference screenshots.

## Android app
- Kotlin + Jetpack Compose
- Login and signup demo screens
- Home, Buy, Bank, Team and Profile tabs
- Fixed conversion display: 1 INR = 1 Coin
- Local sample transaction history
- Demo logout

## Admin panel
Static responsive dashboard in admin/index.html with:
Dashboard, Deposit Orders, Users Directory, Withdrawals, Reports, Home Banners, Products, Broadcast Messages, Commission Setup and Disputes Board.

## Build APK
Push to main or run the GitHub Actions workflow named Android APK. Download the generated airwallet-demo-debug-apk artifact.

## Deploy admin panel
In Vercel, import this GitHub repository and set Root Directory to admin. It is a static HTML app and needs no build command.

## Demo-only notice
All balances, transactions, users and queue records are fictional sample data. This prototype does not accept money, initiate payouts, trade USDT, promise returns, or pay referral commissions. A real financial service requires compliant legal review, secure backend, database, KYC, payment-provider approval, audit logs and fraud controls.
