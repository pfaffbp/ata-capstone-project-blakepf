import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import LoginClient from "../api/loginClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class LoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLogin'], this);
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

        try {
            const validEmail = await this.validEmailFormat(loginInput);
            const login = await this.client.getLogin(validEmail, loginPassInput);

            if (login) {
                this.showMessage(`Logged ${loginInput} successfully!`);
                localStorage.setItem("LoggedIn", JSON.stringify(loginInput));
                window.location.href = "homepage.html";
            } else {
                this.showMessage("incorrect email or password!");
            }
        } catch (error) {
            console.error(error);
            this.errorHandler("Error Logging in! Try again...");
        }

    }

    //-------------------checks if emails is in valid format ------------
    async validEmailFormat(email) {
        if (!this.checkEmail(email)) {
            throw new Error('Invalid email address.');
        }else return email;
    }

    checkEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
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