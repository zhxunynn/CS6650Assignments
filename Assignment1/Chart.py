import pandas as pd
import matplotlib.pyplot as plt

df = pd.read_csv("/tmp/xunyan_cs6650/homework1_client2_32_200000.csv")

df["startTime"] = pd.to_datetime(df["startTime"], unit="ms")

df.set_index("startTime", inplace=True)


throughput = df.resample("S").size()

plt.figure(figsize=(10, 6))
plt.plot(throughput.index, throughput.values, marker="o", linestyle="-")
plt.title("Throughput Over Time")
plt.xlabel("Time")
plt.ylabel("Requests per Second")
plt.grid(True)
plt.xticks(rotation=45)
plt.tight_layout()
plt.show()
