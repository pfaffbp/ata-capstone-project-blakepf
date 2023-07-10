
import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ProfileClient from "../api/profileClient";


/**
 * Logic needed for the view playlist page of the website.
 */
class UpdateProfilePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['updateUserProfile', 'getNotifications'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
    async mount() {
        this.client = new ProfileClient();

    }

    async updateUserProfile() {
        const displayName = document.getElementById('displayName').value;
        const age = document.getElementById('age').value;
        const fullName = document.getElementById('fullName').value;
        const bio = document.getElementById('bio').value;
    
        const user = {
            displayName,
            age,
            fullName,
            bio
        };
    
        await profileClient.updateUser(user, errorHandler);
    
        // Redirect to the profile page after the update
        window.location.href = "profilePage.html";
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
    
    
    errorHandler(error) {
        // Handle the error as needed
        console.error(error);
    }
    

    
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const updateUser = new UpdateProfilePage();
    updateUser.mount();
};

window.addEventListener('DOMContentLoaded', main);
