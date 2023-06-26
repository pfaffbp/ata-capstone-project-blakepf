import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import updateLoginClient from "../api/updateLoginClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class UpdateLoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onUpdateEmail'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new updateLoginClient();
        document.getElementById('updateEmailButton').addEventListener('click', this.onUpdateEmail);

        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onUpdateEmail(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();
       // this.dataStore.set("updateEmail", null);
        const emailInput = document.getElementById("email-entry").value;
        const newEmailInput = document.getElementById("newEmail-entry").value;
        const newEmailConfirmInput = document.getElementById("newEmailConfirm-entry").value;
        const passInput = document.getElementById("password").value;
        const cPassInput = document.getElementById("confirm-password").value;

        /*   const eyeIcons = document.querySelectorAll(".show-hide");
           eyeIcons.forEach((eyeIcon) => {
               eyeIcon.addEventListener("click", () => {
                   const pInput = eyeIcon.parentElement.querySelector("input"); //getting parent element of eye icon and selecting the password input
                   if (pInput.type === "password") {
                       eyeIcon.classList.replace("bx-hide", "bx-show");
                       return (pInput.type = "text");
                   }
                   eyeIcon.classList.replace("bx-show", "bx-hide");
                   pInput.type = "password";
               });
           });*/


        try {
            await this.validateEmailInput(newEmailInput, newEmailConfirmInput);
            await this.validatePasswordInput(passInput, cPassInput);
            const emailUpdate = await this.client.updateEmailByEmail(emailInput, newEmailInput, passInput);
            //this.dataStore.set('emailUpdate', emailInput)
            this.showMessage(`Email: ${newEmailInput} updated successfully!`);
            window.location.href = "homepage.html";


        } catch (error) {
            console.error(error);
            this.errorHandler("Error Updating Email! Try again...");

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
    const updateLoginPage = new UpdateLoginPage();
    updateLoginPage.mount();
};

window.addEventListener('DOMContentLoaded', main);
