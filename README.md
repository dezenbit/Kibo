# Kibo

Aplicación Android nativa para crear y seguir hábitos diarios: rachas, calendario,
recordatorios locales y estadísticas — sin cuenta, sin internet, 100% privada
(todo se guarda en el propio teléfono).

## Características

- Crear hábitos con nombre, emoji, color, categoría y días de la semana en los que aplican
- Dos tipos de hábito: **Sí/No** (check simple) o **Cantidad** (meta numérica diaria, ej. "8 vasos de agua")
- Marcar como completado el día de hoy (o cualquier día pasado) desde la pantalla
  principal, el detalle del hábito o el **widget de pantalla de inicio**
- Cálculo automático de **racha actual** y **mejor racha histórica**, respetando
  la frecuencia configurada
- **Modo vacaciones**: pausa un hábito unos días sin romper tu racha
- **Notas diarias** por hábito
- Calendario semanal interactivo y **mapa de calor** (estilo GitHub) por hábito
- **Reordenar hábitos** manualmente (subir/bajar) y filtrarlos por categoría
- Recordatorios diarios con notificación a la hora que elijas (`AlarmManager` +
  `BroadcastReceiver`, sobreviven reinicios del teléfono)
- **Widget de pantalla de inicio**: hasta 5 hábitos de hoy con check directo, sin abrir la app
- Pantalla de estadísticas: consistencia de la semana, mejor día de la semana y
  ranking de hábitos por racha
- **Exportar/importar copia de seguridad** en un archivo `.json`
- Pantalla de **Ajustes**: tema (sistema/claro/oscuro), color dinámico (Android 12+),
  vibración al completar
- Pantalla **Acerca de** con información del desarrollador y contacto
- Material Design 3, modo oscuro automático, 100% offline y privado

## Arquitectura

- **Kotlin + Jetpack Compose** (Material 3) para toda la interfaz
- **MVVM**: cada pantalla tiene su `ViewModel` con `StateFlow`
- **Room** para persistencia local (`habits` y `habit_completions`)
- **DataStore Preferences** para los ajustes de la app (tema, vibración, etc.)
- **Navigation Compose** para moverse entre pantallas
- **AppWidgetProvider + RemoteViews** para el widget de pantalla de inicio
- Backup/restauración en JSON usando `org.json` (sin dependencias externas)
- Sin frameworks de inyección de dependencias (para mantener el proyecto simple
  de leer): una `ViewModelFactory` genérica inyecta los repositorios a mano

```
app/src/main/java/com/dezenbit/habitos/
├── data/            # Entidades Room, DAO, base de datos, repositorios, backup
├── notifications/   # Canal de notificaciones, AlarmManager, receivers
├── widget/          # AppWidgetProvider y receiver de toques del widget
├── ui/
│   ├── theme/       # Colores, tipografía, tema Material 3
│   ├── components/  # Composables reutilizables (tarjeta de hábito, heatmap, etc.)
│   ├── screens/     # home, addedit, detail, stats, reorder, settings (UI + ViewModel)
│   └── navigation/  # NavGraph
├── util/            # Cálculo de rachas, fechas y vibración
├── HabitApplication.kt
└── MainActivity.kt
```

## Cómo abrir y ejecutar el proyecto

1. Abre **Android Studio** (versión Koala/2024.1 o más reciente recomendada).
2. `File > Open...` y selecciona la carpeta `Habitos/`.
3. Espera a que Gradle sincronice — descargará automáticamente las dependencias
   (necesita conexión a internet la primera vez).
4. Conecta un dispositivo o crea un emulador (API 24 o superior).
5. Pulsa ▶ Run.

No se necesita ninguna clave de API ni configuración adicional: la app funciona
completamente offline.

## Requisitos técnicos

- minSdk 24 (Android 7.0) — targetSdk 35
- Gradle 8.7 / Android Gradle Plugin 8.5.2 / Kotlin 1.9.24
- Core library desugaring activado (para poder usar `java.time` desde API 24)


## Posibles mejoras futuras

- Categorías con color propio (además del filtro por texto)
- Comparar el progreso de varios hábitos en un mismo gráfico
- Backup automático en Google Drive
- Modo multi-perfil (hábitos personales vs. familiares)
