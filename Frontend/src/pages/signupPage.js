import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import SignupClient from "../api/signupClient";
import bcrypt from 'bcryptjs';



/**
 * Logic needed for the view playlist page of the website.
 */
class SignupPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods([ 'onCreateLogin'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new SignupClient();
        document.getElementById('createUser').addEventListener('click', this.onCreateLogin);

       // await this.alreadyLoggedIn();



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

   /*     const emailInput = document.getElementById('email-entry').value;
        const passInput = document.getElementById('password').value;
        const cPassInput = document.getElementById('confirm-password').value;*/
        try {
         //   console.log("before hash = " + passInput);
            await this.validateUserInput(passInput, cPassInput);
            const hashedPassword = await bcrypt.hash(passInput, 10);
           // console.log("hashedPassword  = " + hashedPassword)
            const login = await this.client.createLogin(emailInput, hashedPassword);
            this.dataStore.set('login', emailInput)
         //   this.showMessage(`Login ${emailInput} created successfully!`);
            /*console.log('Login', login)*/
           // window.location.href = "login.html";
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
    const signupPage = new SignupPage();
     await signupPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
