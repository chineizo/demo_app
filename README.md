# demo_app
Location Acquisition Service

# Background Work Explanation
WorkManager was used in scheduling a periodic task to acquire GPS data because it is the recommended
means for background processing or executing persistent tasks.

# Selected method Collecting Location Data
Used Google Play  Services to facilitate Location acquisition because it is more efficient on power
consumption while acquiring GPS fixes.

Used Access Background Location permission to collect GPS data in the background due to the restrictions
imposed from API 26 (Oreo) and above. 