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
        this.bindClassMethods(['onUpdatePassword', 'toggle'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new updatePasswordClient();
        document.getElementById('updatePasswordButton').addEventListener('click', this.onUpdatePassword);
        document.getElementById('eyes').addEventListener('click', this.toggle);
        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onUpdatePassword(event) {
        event.preventDefault();

        const emailInput = document.getElementById("updatePassword-email-Login").value;
        const passInput = document.getElementById("loginPassword").value;
        const newPassInput = document.getElementById("newLoginPassword").value;
        const newPassConfirmInput = document.getElementById("newLoginPassword-Confirm").value;

        try {
            await this.validatePasswordInput(newPassInput, newPassConfirmInput);
            const validEmail = await this.validEmailFormat(emailInput)
            const hashedNewPassword = await bcrypt.hash(newPassInput, 10);
            const updatePassword = await this.client.updatePasswordByEmail(validEmail, passInput, hashedNewPassword);
            this.showMessage(`Password for: ${emailInput} updated successfully!`);
            window.location.href = "homepage.html";

        } catch (error) {
            console.error(error);
            this.errorHandler("Error Updating! Try again...");
        }
    }

    async toggle(event){
        const container = document.querySelector(".container-1"),
            pwShowHide = document.querySelectorAll(".showHidePw"),
            pwFields = document.querySelectorAll(".password");

//   js code to show/hide password and change icon
        pwShowHide.forEach(eyeIcon =>{
            eyeIcon.addEventListener("click", ()=>{
                pwFields.forEach(pwField =>{
                    if(pwField.type ==="password"){
                        pwField.type = "text";

                        pwShowHide.forEach(icon =>{
                            icon.classList.replace("bx-lock-alt", "bx-lock-open-alt");
                        })
                    }else{
                        pwField.type = "password";

                        pwShowHide.forEach(icon =>{
                            icon.classList.replace("bx-lock-open-alt", "bx-lock-alt");
                        })
                    }
                })
            })
        })
    }

    //-------------------checks if passwords match ------------
    async validatePasswordInput(password, confirmPassword) {
        if (!this.validatePassword(password, confirmPassword)) {
            alert("passwords must match");
            throw new Error('Passwords must match.');
        }
    }

    validatePassword(password, newPassword) {
        return password.length >= 8 && password === newPassword;
    }


async validEmailFormat(email) {
    if (!this.checkEmail(email)) {
        alert("invalid email");
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
    const updatePasswordPage = new UpdatePasswordPage();
    await updatePasswordPage.mount();
};

window.addEventListener('DOMContentLoaded', main);