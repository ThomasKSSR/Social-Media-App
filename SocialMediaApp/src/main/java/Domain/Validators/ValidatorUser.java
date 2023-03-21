package Domain.Validators;

import Domain.User;

public class ValidatorUser<T> implements Validator<T>{

    /**
     * chechs if the data of a user is valid
     * @param entity User to be checked
     * @throws ValidationException if the user's data is not valid
     */
    @Override
    public void validate(T entity) throws ValidationException {
        User user = (User)entity;
        String ok = "";
        if(user.getID() <0){
            ok+= "Invalid ID!";

        }
        if(user.getPassword() == "" || user.getPassword() ==" "){
            ok+="Invalid Password";
        }
        if(user.getName() == "" || user.getName() == " " || user.getName() == "Drop Table" || user.getName() == "Null"){
            ok += "Invalid Name";
        }
        if(ok != ""){
            throw new ValidationException(ok);
        }
    }
}
