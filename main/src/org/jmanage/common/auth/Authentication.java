package org.jmanage.common.auth;

/**
 * Authentication is a generic interface to create/update/delete users and
 * also to authenticate user based on username/password.
 *
 * date:  Jun 4, 2004
 * @author	Rakesh Kalra
 */
public interface Authentication {

    /**
     * Authenticates the user based on the given username/password. This method
     * returns null if the authentication failed.
     *
     * @param username  username identifying the user to be authenticated
     * @param password  password for the given username
     * @return  UserProfile object containing user information, if
     *              authentication was successfull; null otherwise.
     */
    public UserProfile authenticate(String username, String password);

    /**
     * Create a new user based on the given UserProfile object and password.
     *
     * @param userProfile   UserProfile object containing user information.
     * @param password      password for given user
     * @return  UserProfile object containing information about newly created
     *             user.
     * @throws  UserNameAlreadyExistsException  if the username is already used
     */
    public UserProfile createNewUser(UserProfile userProfile, String password)
            throws UserNameAlreadyExistsException;

    /**
     * Removes the user identified by the given username. This user can no
     * longer login.
     *
     * @param username  username identifying the user to be removed
     * @return  UserProfile of the user that was removed
     * @throws  UserNotFoundException   if the user identified by the given
     *          username is not found
     */
    public UserProfile removerUser(String username)
            throws UserNotFoundException;

    /**
     * Updates the userProfile.
     *
     * @param userProfile   the UserProfile that needs to be updated
     * @return  updated UserProfile object
     */
    public UserProfile updateUser(UserProfile userProfile);
}
