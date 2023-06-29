import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import LoginClient from "../api/loginClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class LoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods([  'onLogin'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new LoginClient();

        document.getElementById('LoginUser').addEventListener('click', this.onLogin);
       // await this.alreadyLoggedIn();



    }

    // Render Methods --------------------------------------------------------------------------------------------------



        async onLogin(event) {
            event.preventDefault();
            this.dataStore.set("LoggedIn", null);
            const loginInput = document.getElementById("email-Login").value;
            const loginPassInput = document.getElementById("loginPassword").value;
            /*const  form = document.querySelector("form"),
                loginField = form.querySelector(".login-email-field"),
                loginInput = loginField.querySelector("email").value,
                passField = form.querySelector("login-password"),
                loginPassInput = passField.querySelector("password").value;*/
            try {
                console.log("email: " + loginInput);
                console.log("login pass input: " + loginPassInput);
                const login = await this.client.getLogin(loginInput, loginPassInput);
                console.log("login " + login);
                if (login) {
                    this.showMessage(`Logged ${loginInput} successfully!`);
                    localStorage.setItem("LoggedIn", JSON.stringify(loginInput));
                  //  console.log(localStorage.getItem("LoggedIn"));
                    window.location.href = "homepage.html";
                } else {
                    this.showMessage("incorrect email or password!");
                }
            } catch (error) {
                console.error(error);
                this.errorHandler("Error Logging in! Try again...");
            }

        }

    async validateUserInput(passInput, cPassInput) {
        if (!this.validatePassword(passInput, cPassInput)) {
            throw new Error('Passwords must match.');
        }
        }

  validateEmail(email, confirmEmail){
        return email === confirmEmail;
  }

    validatePassword(passInput, cPassInput) {
        return passInput === cPassInput;
    }



}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const loginPage = new LoginPage();
     loginPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
