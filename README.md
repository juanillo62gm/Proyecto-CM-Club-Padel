# Proyecto CM
## Club Padel

[App de Ejemplo](https://play.google.com/store/apps/details?id=com.playtomic)

1. Inicio
   - Las que tienes ya reservadas*
   - Reserva clase
   - Reserva pista
   - Horario

2. Perfil
   - Nombre
   - Apellidos
   - Móvil
   - Correo
   - Ajustes

# Configuración Firebase 
- Default KeyStore Password: android
- JGM Certificate fingerprints:
  SHA1: 89:9F:6A:57:61:4F:6E:A5:AC:29:D3:AA:80:F6:1A:60:4F:5A:64:C5
  SHA256:67:A6:32:91:FC:6F:02:93:99:0F:14:A7:FC:72:75:C0:9C:9B:FE:E4:11:51:6E:2E:1D:9E:2A:8C:54:12:19:71

- APT Certificate fingerprints:
  SHA1: 7A:3A:F5:83:D4:57:ED:66:32:5E:01:BB:FA:1E:70:45:8A:53:28:C1
  SHA256: FC:52:FE:8D:09:C6:DF:4C:93:96:0D:24:B0:FB:33:14:C7:77:BA:E1:62:9D:C1:4D:86:DB:77:2D:2D:30:0A:2C


# Database
- Usuarios:
   - idUsuario
   - Nombre
   - Apellidos
   - Email
   - Teléfono

- Reservas:
   - idReserva
   - Horario   Ej: (04/05/2021 18:00:00)
   - idUsuario
   - Pista

- Partidos
   - idPartido
   - idReserva
   - idUsuario1
   - idUsuario2
   - idUsuario3
   - idUsuario4
   - Parejas
   - Resultado

# Comentarios 
Para la tabla partidos (Parejas):
- 0: J1 y J2 vs J3 y J4
- 1: J1 y J3 vs J2 y J4
- 2: J1 y J4 vs J2 y J3

# Tutoriales

- Firebase Auth
  1. https://www.youtube.com/watch?v=dpURgJ4HkMk
  2. https://www.youtube.com/watch?v=IwdjCApjIzA
  3. https://www.youtube.com/watch?v=bBJF1M5h_UU
  4. https://www.youtube.com/watch?v=YO6Xx5glLPE
      - Facebook Auth
  5. https://developers.facebook.com/docs/facebook-login/access-tokens/
  6. https://github.com/firebase/snippets-android/blob/7484726ae6d9d2b1cfe9d0545e1c80f98a359a71/auth/app/src/main/java/com/google/firebase/quickstart/auth/FacebookLoginActivity.java#L88-L94
  7. https://stackoverflow.com/questions/16314651/customize-android-facebook-login-button
      - Solución al botón predeterminado
   

- Firebase Database
  1. https://www.youtube.com/watch?v=2Zui7Te_zc4
  2. https://www.youtube.com/watch?v=ADzghd25iXQ
  3. https://www.youtube.com/watch?v=5j81RmRQFU0
  4. https://www.youtube.com/watch?v=HTsIBm4kjqE
  5. https://www.youtube.com/watch?v=wa8OrQ_e76M
