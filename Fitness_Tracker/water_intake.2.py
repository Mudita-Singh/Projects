class WaterIntakeTracker:
    def __init__(self):
        self.intake = 0

    def track_intake(self, amount):
        self.intake += amount

    def display_intake(self):
        print(f"Water Intake: {self.intake} ml")

# Create an instance of WaterIntakeTracker
tracker = WaterIntakeTracker()

while True:
    # Track water intake
    amount = float(input("Enter amount of water intake (ml), or 0 to stop: "))
    if amount == 0:
        break  # Exit the loop if the user enters 0
    tracker.track_intake(amount)

# Display total intake
tracker.display_intake()
