import com.projects.Card;
import com.projects.Hand;
import org.junit.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveFourtet {

    Hand hand = new Hand("test", true);


    {
        hand.addCard(new Card("7", "Spades"));
        hand.addCard(new Card("7", "Hearts"));
        hand.addCard(new Card("7", "Diamonds"));
        hand.addCard(new Card("7", "Clubs"));
    }
    @Before
    {
        GoFish
    }
    // Create a Hand, add four 7s then run Remove4OfAKind("7")
    @Test
    public class CheckRemoveFourOfAKind {
        // Assert if Hand after function is empty
        assertEquals(true, hand.isEmpty());
    }

}
