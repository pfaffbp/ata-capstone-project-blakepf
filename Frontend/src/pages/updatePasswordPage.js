import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import updatePasswordClient from "../api/updatePasswordClient";
import bcrypt from 'bcryptjs';

/**
 * Logic needed for the view playlist page of the website.
 */
class UpdatePasswordPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods([ 'onUpdatePassword'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new updatePasswordClient();
        document.getElementById('updatePasswordButton').addEventListener('click', this.onUpdatePassword);
        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onUpdatePassword(event) {
        event.preventDefault();
      //  this.dataStore.set("updatePassword", null);
        const emailInput = document.getElementById("updatePassword-email-Login").value;
        const passInput = document.getElementById("loginPassword").value;
        const newPassInput = document.getElementById("newLoginPassword").value;
        const newPassConfirmInput = document.getElementById("newLoginPassword-Confirm").value;
        /*const  form = document.querySelector("form"),
            loginField = form.querySelector(".login-email-field"),
            loginInput = loginField.querySelector("email").value,
            passField = form.querySelector("login-password"),
            loginPassInput = passField.querySelector("password").value;*/
        try {
            await this.validatePasswordInput(newPassInput, newPassConfirmInput);
            const hashedNewPassword = await bcrypt.hash(newPassInput, 10);
            const updatePassword = await this.client.updatePasswordByEmail(emailInput, passInput, hashedNewPassword);
            this.showMessage(`Password for: ${emailInput} updated successfully!`);
            window.location.href = "homepage.html";

        } catch (error) {
            console.error(error);
            this.errorHandler("Error Updating! Try again...");
        }
    }


    async validatePasswordInput(password, confirmPassword) {
        if (!this.validatePassword(password, confirmPassword)) {
            throw new Error('Passwords must match.');
        }
    }

    async validateEmailInput(email, confirmEmail) {
        if (!this.validateEmail(email, confirmEmail)) {
            throw new Error('Emails must match.');
        }
    }


    validateEmail(email, confirmEmail) {
        return email === confirmEmail;
    }

    validatePassword(password, newPassword) {
        return password === newPassword;
    }

    // Hide and show password



}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updatePasswordPage = new UpdatePasswordPage();
    updatePasswordPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
