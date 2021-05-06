# Debt Collector
**Android app, made as a project for mobile programming course.**


App was created in Android Studio in JAVA programming language.
Main puropse of the app is to manage debt of user's friends. 
### App has three activities:
- **Debt list:**
Displays list of debtors. Each position has debtor name and debt. Debtor list is stored in local db (sqlite). Total debt of all debtors is also displayed.
Choosing element on the list will display edit activity, long press will display alert window for removong debtor from the list.
In this activity user has also possibility to add new debtor (by touching appropriate button).

- **Add/edit debtor**
Provides functionality of adding new debtor and editing existing one.    
Ekran ten umożliwia nadanie/zmianę nazwy dłużnika oraz jego długu. User can accept or decline changes.
In this activity user can simulate debt payment (by clicking appropriate button) or inform debtor about his debt (sharing text information).
- **Simulate payment**
User can set payment speed (PLN/s) and interest rate.
By touching play button user can start payment simulation. Simulation can be stopped at any time by the user or automatically when debt is payed. Then app displays info with calculated total amount of iterest payed.

App implements ContentProvider.
