# MemoriesApp

Some issues could occur when using Android Emulator due to use NETWORK_PROVIDER instead of GPS_PROVIDER on some LocationManager calls, if so, please, change next code lines:

  - Android Subject Release:

    - MainActivity - Line 79: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

    - ManageMemory - Line 85: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

    - MapActivity - Line 51: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

  - BBDD Subject Release:
  
    - MainActivity - Line 86: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER
    
    - ManageMemory - Line 90: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER
    
    - MapActivity - Line 55: LocationManager.NETWORK_PROVIDER -> LocationManager.GPS_PROVIDER

Notifications only work on real devices, we do not know why because when we tested the projects we did it only on emulator...


Copyright - USJ - Fran & Mar (2019-20)
