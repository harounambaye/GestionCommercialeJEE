# 🛒 Gestion Commerciale JEE - Sama Boutik

## 📌 Description

**GestionCommercialeJEE (Sama Boutik)** est une application de gestion commerciale développée en **Java EE**.
Elle permet de gérer les activités d’une boutique ou d’une entreprise commerciale : produits, commandes, stock, utilisateurs, livraisons, etc.

---

## ⚙️ Technologies utilisées

* ☕ Java EE (EJB)
* 📦 Maven
* 🗄️ JPA / Hibernate (Persistence API)
* 🐘 PostgreSQL
* 🎨 PrimeFaces (JSF UI Framework)
* 🌐 Frontend séparé (`sama_boutik_frontend`)
* 🧰 IDE recommandé : Eclipse

---

## 📁 Architecture du projet

Le projet suit une architecture en couches :

```id="gtkqqc"
sama_boutik_ejb/
│
├── dao/          → Accès aux données (DAO)
├── entity/       → Entités JPA (tables DB)
├── service/      → Logique métier
├── dto/          → Objets de transfert
├── META-INF/     → Configuration (persistence.xml)
└── pom.xml       → Configuration Maven
```

---

## 🧩 Fonctionnalités principales

* 👤 Gestion des utilisateurs (authentification, rôles, permissions)
* 🛍️ Gestion des produits et catalogues
* 📦 Gestion des stocks
* 🧾 Gestion des commandes
* 🚚 Gestion des livraisons
* 🏢 Gestion des entités commerciales
* 📊 Statistiques (commandes, stock)

---

## 🚀 Installation & Exécution

### 1️⃣ Cloner le projet

```bash id="qdtjzi"
git clone https://github.com/harounambaye/GestionCommercialeJEE.git
cd GestionCommercialeJEE
```

---

### 2️⃣ Configurer la base de données

* Créer une base PostgreSQL
* Configurer le fichier :

```id="b0j80p"
sama_boutik_ejb/ejbModule/META-INF/persistence.xml
```

👉 Modifier :

* URL de connexion
* username
* password

---

### 3️⃣ Build avec Maven

```bash id="qdmzrn"
mvn clean install
```

---

### 4️⃣ Déploiement

Déployer le fichier `.jar` généré sur un serveur Java EE :

* WildFly
* GlassFish
* Payara

---

## 🔐 Authentification

Le projet inclut un service :

```id="hha29p"
AuthService
```

Qui permet :

* Connexion utilisateur
* Gestion des accès (rôles / permissions)

---

## 📦 Modules

### Backend

* `sama_boutik_ejb` → logique métier + accès données

### Frontend

* `sama_boutik_frontend` → interface utilisateur (PrimeFaces)

---

## ⚠️ Remarques

* Le dossier `target/` est généré automatiquement (build Maven)
* Les fichiers `.classpath`, `.project` sont liés à Eclipse
* Le frontend est inclus comme sous-module Git

---

## 👨‍💻 Auteur

* **Harouna MBAYE**

---

## 📄 Licence

Projet académique / personnel – utilisation libre pour apprentissage.

---
