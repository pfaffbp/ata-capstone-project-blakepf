import BaseClass from "../util/baseClass";
import axios from 'axios'
let displayName = localStorage.getItem('displayName')


/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class ProfileClient extends BaseClass {

    constructor(props = {}){
        super();
        const methodsToBind = ['clientLoaded', 'getUserData', 'getNotifications'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);

    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     * @param client The client that has been successfully loaded.
     */
    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")){
            this.props.onReady();
        }
    }

    /**
     * Gets the User for the given ID.
     * @param id Unique identifier for a concert
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */
    async getUserData( errorCallback){

        try {
            const response = await this.client.get(`/user/${displayName}/searchByDisplayName/`);
            return response.data;
        }catch (error){
            this.handleError('getUserData', error, errorCallback)
        }
    }
    async updateUser(user, errorCallback) {
        try {
            // Make the API call to update the user on the backend
            const response = await this.client.put(`/user/updateUser/`, user);
            // Handle the response as needed
            console.log("User updated successfully:", response.data);
        } catch (error) {
            this.handleError('updateUser', error, errorCallback);
        }
    }

    async getNotifications (displayName, errorCallback) {
        try {
            const response = await this.client.get(`notification/getNotification/${displayName}`)
            return response.data;
        } catch (error) {
            this.handleError('getNotifications', error, errorCallback)
        }
    }



    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
}
