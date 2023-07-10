import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import LoginClient from "../api/loginClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class LoginPage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onLogin', 'toggle', 'getNotifications'], this);
        this.dataStore = new DataStore();


    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new LoginClient();
        document.getElementById('logout').addEventListener('click', this.Logout);
        document.getElementById('LoginUser').addEventListener('click', this.onLogin);
        document.getElementById('eyes').addEventListener('click', this.toggle);
        this.onLoad();
        // await this.alreadyLoggedIn();


    }

    // Render Methods --------------------------------------------------------------------------------------------------


    async onLogin(event) {
        event.preventDefault();
        const loginInput = document.getElementById("email-Login").value;
        const loginPassInput = document.getElementById("loginPassword").value;

        try {
            const validEmail = await this.validEmailFormat(loginInput);
            const login = await this.client.getLogin(validEmail, loginPassInput);

            if (login) {
                const displayName = await this.client.getUserDisplayName(loginInput);
                localStorage.setItem("displayName", displayName);
                localStorage.setItem("LoggedIn", loginInput);
                window.location.href = "landingPage.html";



            } else {
                this.showMessage("incorrect email or password!");
            }
        } catch (error) {
            console.error(error);
            this.errorHandler("Error Logging in! Try again...");
        }

    }

    async toggle(event) {
        const container = document.querySelector(".container-1"),
            pwShowHide = document.querySelectorAll(".showHidePw"),
            pwFields = document.querySelectorAll(".password");

        //   js code to show/hide password and change icon
        pwShowHide.forEach(eyeIcon => {
            eyeIcon.addEventListener("click", () => {
                pwFields.forEach(pwField => {
                    if (pwField.type === "password") {
                        pwField.type = "text";

                        pwShowHide.forEach(icon => {
                            icon.classList.replace("bx-lock-alt", "bx-lock-open-alt");
                        })
                    } else {
                        pwField.type = "password";

                        pwShowHide.forEach(icon => {
                            icon.classList.replace("bx-lock-open-alt", "bx-lock-alt");
                        })
                    }
                })
            })
        })
    }

    //-------------------checks if emails is in valid format ------------
    async validEmailFormat(email) {
        if (!this.checkEmail(email)) {
            alert("invalid email");
            throw new Error('Invalid email address.');
        } else return email;
    }

    checkEmail(email) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    }
    async Logout(event) {
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
    }

    async onLoad() {
        let user = localStorage.getItem('displayName')
        let LoggedInArea = document.getElementById('userLoggedIn');

        let notification = this.client.getNotifications(user);
        console.log(notification);

        if (user != null) {

            document.getElementById("bell").classList.remove("hide");
            document.getElementById("bell").addEventListener("click", this.getNotifications);

            LoggedInArea.innerHTML = user;
        } else
            LoggedInArea.innerHTML = "Login";
    }

    async getNotifications() {
        console.log("In getNotifications");                                 // Checks to see if it makes it to this method when clicking the bell.
        let user = localStorage.getItem("displayName");                     // Grabbing the user name. 
        console.log(user);                                                  // Logging the user name to check and see if it pulls the correct one. 

        const response = await this.client.getNotifications(localStorage.getItem("displayName"), this.errorHandler)         // Sends a get request to the Lambda API.

        let notificationDiv = document.getElementById("notification-items");     // Create variable to point to notification dropdown.
        let notificationHtml = "";                                               // Create variable of an empty string.

        for (let i = 0; i < response.length; i++) {
            console.log("In fori loop to gather response details");
            console.log(response.length)                                         // Verify length
            notificationHtml +=
                `<div class = "notification"><img src = "https://i.pinimg.com/736x/f8/84/7b/f8847b5a92b0e321d6df26ebaee9b39c.jpg" class = "notification-img"> 
            <p> ${response[i].userRequest} </p>
        </div> `
            console.log(response[i].userRequest)
        }

        notificationDiv.innerHTML = notificationHtml;                              // Setting the newly created html to the innerHtml of the notification dropdown.
        console.log(notificationDiv.innerHTML)
        console.log(response);
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