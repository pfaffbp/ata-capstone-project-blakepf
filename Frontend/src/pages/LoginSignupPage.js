import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import LoginSignupClient from "../api/LoginSignupClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class LoginSignupPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods([ 'onCreateLogin'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new LoginSignupClient();
        document.getElementById('createUser').addEventListener('click', this.onCreateLogin);



    }

    // Render Methods --------------------------------------------------------------------------------------------------



    async onCreateLogin(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
        this.dataStore.set("createLogin", null);
        const form = document.querySelector("form"),
            emailField = form.querySelector(".email-field"),
            emailInput = emailField.querySelector(".email").value,
            passField = form.querySelector(".create-password"),
            passInput = passField.querySelector(".password").value,
            cPassField = form.querySelector(".confirm-password"),
            cPassInput = cPassField.querySelector(".cPassword").value;


        try {
            await this.validateUserInput(passInput, cPassInput);
            const login = await this.client.createLogin(emailInput, passInput);
            this.dataStore.set('login', login)
            console.log('Login', login)


        }catch (error) {
            console.error(error);
            this.errorHandler("Error creating Login! Try again...");

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
    const loginSignupPage = new LoginSignupPage();
     loginSignupPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
