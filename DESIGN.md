# UI/UX & Design System Specifications

This document outlines the strict UI/UX guidelines, design tokens, and Component specifications for the Jetpack Compose presentation layer of the Rick & Morty Multiverse Explorer app. The goal is a highly polished, production-grade interface ready for Play Store publication.

---

## 1. Color Palette & Theming (Material Design 3)

The application utilizes a dark-themed, cinematic sci-fi palette reflecting the chaotic, portal-hopping essence of the show, balanced with modern accessible contrast steps.

### Dark Theme Tokens
* **Primary (Portal Green):** `#39FF14` (Neon/Fluorescent Green)
* **OnPrimary:** `#003B00`
* **Secondary (Cosmic Purple):** `#8A2BE2` (Deep Vibrant Purple)
* **Background:** `#0B0C10` (Near Black / Deep Space Void)
* **Surface:** `#1F2833` (Dark Slate Gray for cards and containers)
* **OnSurface:** `#C5C6C7` (Soft Light Gray for readable body text)
* **PrimaryVariant / Accent (Dimension Blue):** `#00D2FF` (Bright Cyan)

### Typography
* **Headers/Titles:** `FontFamily.SansSerif` or system default, Bold, tracking tight.
* **Body Text:** `FontFamily.Default`, regular weights with precise line heights for optimal reading flow.

---

## 2. Component Design Guidelines

### A. Character List Item Card
* **Structure:** An elevated or outlined card featuring a split horizontal layout. Left side houses the character image; right side lists core metadata.
* **Image Container:** Fixed aspect ratio or box layout using Coil's `AsyncImage`. Must feature a subtle crossfade animation on load.
* **Status Indicator:** A row item showing a small colored dot representing the character's status:
    * Green (`#4CAF50`) for Alive
    * Red (`#F44336`) for Dead
    * Gray (`#9E9E9E`) for Unknown
* **Interaction:** Clicking the card must trigger an ripple effect (`LocalRippleConfiguration`) and fire the `OnCharacterClicked(id)` UI Event.

### B. Detail Screen Layout
* **Hero Section:** Large parallax or collapsing header showcasing an expanded character portrait.
* **Information Cards:** Grouped categories (e.g., "Vital Statistics", "Origin & Location", "Episodes Featured") using individual Material 3 surface containers with consistent padding (`16.dp`).

---

## 3. Advanced UI States & Micro-interactions

Every view model state must visually map to a professional state representation:

### 1. Loading States (Shimmer/Skeletons)
* Do **not** use raw, infinite loop `CircularProgressIndicator` screens for content feeds.
* Implement a **Shimmer Effect Brush** over mock list card skeletons. The shimmer must animate continuously from top-left to bottom-right using a linear easing infinite transition block.

### 2. Error Recovery (Snackbars & Skeletons)
* When a network exception occurs but the local database contains cached records, display the cache silently and trigger a `SnackbarHost` message stating: *"Displaying offline data. Please check your connection."* accompanied by a "Retry" button action.
* If both network and local cache are missing, render an explicit error screen featuring a vector illustration and a prominent "Try Again" button.

### 3. Pull-To-Refresh
* The List screen must implement Jetpack Compose's modern Pull-to-Refresh pattern (`PullToRefreshBox` or `rememberPullToRefreshState`). Triggers a fresh remote API network synchronize request, bypassing local storage matching rules temporarily.

---

## 4. Compose Motion & Motion Transitions

* **Navigation Transitions:** Configure the `NavHost` destination definitions to leverage sleek slide-in/slide-out animations across horizontal axes rather than standard sudden cuts.
    * **Enter Transition:** `slideInHorizontally(initialOffset = { it }) + fadeIn()`
    * **Exit Transition:** `slideOutHorizontally(targetOffset = { -it }) + fadeOut()`
* **State Changes:** Wrap visibility toggles (like showing/hiding search bars or search filter tags) inside `AnimatedVisibility` layouts with clean spring transitions (`spring(stiffness = Spring.StiffnessLow)`).