class WaterIntakeTracker:
    def __init__(self):
        self.intake = 0

    def track_intake(self, amount):
        self.intake += amount

    def display_intake(self):
        print(f"Water Intake: {self.intake} ml")

def track_water_intake():
    amount = float(input("Enter amount of water intake (ml): "))
    return amount

# Create an instance of WaterIntakeTracker
tracker = WaterIntakeTracker()

# Track water intake
amount = track_water_intake()

# Update intake
tracker.track_intake(amount)

# Display intake
tracker.display_intake()
