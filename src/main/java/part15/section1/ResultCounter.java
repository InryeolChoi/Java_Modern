package part15.section1;

import lombok.*;

@NoArgsConstructor
@Getter
public class ResultCounter {
    private int successCount = 0;
    private int completedCount = 0;

    // mutex
    public synchronized void addSuccess() {
        successCount++;
    }

    public synchronized void addCompleted() {
        completedCount++;
    }

}
