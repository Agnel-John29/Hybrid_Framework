# Hybrid Automation Framework (Java + Selenium + TestNG)

This repository contains a powerful hybrid automation framework designed for scalable, maintainable, and reusable test automation across web and API layers. Built with TestNG, Selenium WebDriver, and Java, the framework integrates custom utilities for reporting, Excel-based data handling, and environment configurations.

##  Features

-  Keyword-Driven + Hybrid Framework Structure
-  Selenium WebDriver for UI Automation
-  TestNG for test orchestration
-  API Testing Workbench using RestAssured (modular base)
-  Custom HTML Reporter with detailed step logs
-  Excel-based test data handling
-  Page Object Model (POM) with reusable actions
-  Ready for CI/CD integration

## 📁 Project Structure

src/
├── main/
│ ├── java/
│ │ ├── Design/ # Keyword actions for Web
│ │ ├── ImplementationBase/ # Base logic for Web/API
│ │ ├── ProjectSpecificBase/ # Project-configured base
│ │ ├── pages/ # Page Object classes
│ │ └── utils/ # Reporting & utilities
│ └── resources/ # Config files, test data
├── test/
│ └── java/OR_TC/ # Test scenarios


## � Getting Started

- Clone the repo
- Import as a Maven project
- Update config in `/resources`
- Run via TestNG XML or CI tools

##  Author

Developed by **Agnel J**, Automation Architect  
