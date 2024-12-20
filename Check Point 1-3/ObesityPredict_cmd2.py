import os
import pandas as pd
import numpy as np
import joblib

# Path relatif ke folder model
base_dir = os.path.dirname(__file__)  # Mendapatkan lokasi file Python saat ini
model_path = os.path.join(base_dir, 'gradient_boosting_model.pkl')
scaler_path = os.path.join(base_dir, 'scaler.pkl')
encoders_path = os.path.join(base_dir, 'label_encoders.pkl')

# Load Saved Model and Preprocessor
gb_model = joblib.load(model_path)
scaler = joblib.load(scaler_path)
label_encoders = joblib.load(encoders_path)

def main(args):
    # Parsing parameter argument cmd
    new_data = {
        'Gender': args[0],
        'Age': int(args[1]),
        'Height': float(args[2]),
        'Weight': float(args[3]),
        'family_history_with_overweight': args[4],
        'FAVC': args[5],
        'FCVC': float(args[6]),
        'NCP': float(args[7]),
        'CAEC': args[8],
        'SMOKE': args[9],
        'CH2O': float(args[10]),
        'SCC': args[11],
        'FAF': float(args[12]),
        'TUE': float(args[13]),
        'CALC': args[14],
        'MTRANS': args[15]
    }

    # Convert new data to DataFrame
    new_data_df = pd.DataFrame([new_data])

    # Encoding categorical 
    categorical_columns = ['Gender', 'family_history_with_overweight', 'FAVC', 
                           'CAEC', 'SMOKE', 'SCC', 'CALC', 'MTRANS']

    for col in categorical_columns:
        if col in new_data_df.columns:
            new_data_df[col] = label_encoders[col].transform(new_data_df[col])

    # Scaling numerical kolom
    new_data_scaled = scaler.transform(new_data_df)

    # Prediction
    prediction = gb_model.predict(new_data_scaled)
    predicted_class = prediction[0]

    # Decode target label yang ter-encode
    decoded_prediction = label_encoders['NObeyesdad'].inverse_transform([predicted_class])

    print(f"Predicted Class (Encoded): {predicted_class}")
    print(f"Predicted Class (Decoded): {decoded_prediction[0]}")

if __name__ == "__main__":
    import sys
    main(sys.argv[1:])
