# Rick & Morty Multiverse Explorer

A modern Android application designed as an architectural reference.

## Features
* **Character List:** Fetches characters from `https://rickandmortyapi.com/api/character`.
* **Offline-First Strategy:** Caches API responses in a local Room database. If the network drops, the app serves data from the local DB.
* **Character Detail:** Shows in-depth information about a specific character.
* **Modern Navigation:** Utilizes Type-Safe Compose Navigation passing objects between screens.
* **Professional UI:** Features skeleton loading states, pull-to-refresh, error handling snackbars, and smooth Compose animations.