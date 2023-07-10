import BaseClass from "../util/baseClass";
import axios from 'axios'
import bcrypt from 'bcryptjs';


/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class LoginClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getLogin', 'getUserDisplayName', 'getNotifications'];
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
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    /**
     * Gets the concert for the given ID.
     * @param email
     * @param password
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */

    async getLogin(email, password, errorCallback) {
        try {
            const response = await this.client.post('/login/login', {
                email: email,
            });
            const hashedPassword = response.data.password; // Assuming the password is returned from the server
            const passwordMatch = await bcrypt.compare(password, hashedPassword);
            if (!passwordMatch) {
                throw new Error('Invalid password');
            } else {
                return response.data;
            }
        } catch (error) {
            this.handleError("getLogin", error, errorCallback);
        }
    }

    async getUserDisplayName(email, errorCallback) {
        try {
            const response = await this.client.get(`/user/${email}`);
            return response.data.displayName;
        } catch (error) {
            this.handleError("getUserDisplayName", error, errorCallback)
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
     * @param method
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