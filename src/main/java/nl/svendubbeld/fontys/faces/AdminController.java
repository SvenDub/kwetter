package nl.svendubbeld.fontys.faces;

import nl.svendubbeld.fontys.dto.SecurityGroupDTO;
import nl.svendubbeld.fontys.dto.TweetDTO;
import nl.svendubbeld.fontys.dto.UserDTOSecure;
import nl.svendubbeld.fontys.service.SecurityService;
import nl.svendubbeld.fontys.service.TweetService;
import nl.svendubbeld.fontys.service.UserService;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Named("adminController")
@ViewScoped
public class AdminController implements Serializable {

    @Inject
    private UserService userService;

    @Inject
    private SecurityService securityService;

    @Inject
    private TweetService tweetService;

    private List<UserDTOSecure> users;
    private DataModel<UserDTOSecure> userModel;

    private List<SecurityGroupDTO> securityGroups;

    private List<TweetDTO> tweets;
    private DataModel<TweetDTO> tweetModel;

    @PostConstruct
    public void init() {
        users = userService.findAllAsDTOSecure();
        userModel = new ListDataModel<>(users);

        securityGroups = securityService.findAllGroupsAsDTO();

        tweets = tweetService.findAllAsDTO();
        tweetModel = new ListDataModel<>(tweets);
    }


    public DataModel<UserDTOSecure> getUserModel() {
        return userModel;
    }

    public List<SecurityGroupDTO> getSecurityGroupList() {
        return securityGroups;
    }

    public DataModel<TweetDTO> getTweetModel() {
        return tweetModel;
    }

    public void securityGroupsChanged(ValueChangeEvent e) {
        UserDTOSecure user = userModel.getRowData();
        user.setSecurityGroups((Set<SecurityGroupDTO>) e.getNewValue());

        userService.editSecurityGroups(user);
    }
}
