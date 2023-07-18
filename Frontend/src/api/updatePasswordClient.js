import BaseClass from "../util/baseClass";
import axios from 'axios'
import bcrypt from "bcryptjs";

/**
 * Client to call the MusicPlaylistService.
 *
 * This could be a great place to explore Mixins. Currently the client is being loaded multiple times on each page,
 * which we could avoid using inheritance or Mixins.
 * https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Classes#Mix-ins
 * https://javascript.info/mixins
 */
export default class updatePasswordClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'updatePasswordByEmail'];
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
     * @param newPassword
     * @param errorCallback (Optional) A function to execute if the call fails.
     * @returns The concert
     */


    async updatePasswordByEmail(email, password, newPassword, errorCallback) {
        try {
            const response = await this.client.post('/login/login', {
                email: email,
            });
            console.log("response data: " + response)
            const hashedPassword = response.data.password; // Assuming the password is returned from the server
            const passwordMatch = await bcrypt.compare(password, hashedPassword);

            if (passwordMatch) {
                try {
                    const response = await this.client.put(`/login/changePassword`, {
                        email: email,
                        newPassword: newPassword,
                    });
                    return response.data;
                } catch (error) {
                    this.handleError("updatePassword", error, errorCallback);
                    throw error;
                }
            } else {
                throw new Error('Invalid password');
            }
        } catch (error) {
            this.handleError("Login", error, errorCallback);
            throw error;
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