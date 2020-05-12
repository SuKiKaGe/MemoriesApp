# MemoriesApp

Some issues could occur when using Android Emulator due to use NETWORK_PROVIDER instead of GPS_PROVIDER on some LocationManager functions, if so, please, change next code lines:

  - MainActivity - Line 79: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

  - ManageMemory - Line 85: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

  - MapActivity - Line 51: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

Notifications only work on real devices, we do not know why...
