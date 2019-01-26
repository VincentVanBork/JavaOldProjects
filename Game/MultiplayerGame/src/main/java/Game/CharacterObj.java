package Game;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CharacterObj{

    public int xVel;
    public int yVel;

    public long id;

    public List<Bullet> newBullets;

    public CharacterObj(){}

    public CharacterObj(int xVel, int yVel, long id) {

        this.xVel = xVel;
        this.yVel = yVel;
        this.id = id;
    }

}
