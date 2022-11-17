import SwiftUI
import shared
import OSLog
import CoreLocation

@available(iOS 15.0, *)
struct ContentView: View {
    let greet = Greeting().greeting()
    @State private var weightKgs: String = ""
    @State private var heightCMs: String = ""
    @State private var bmiShow : Bool = false
    @State private var bmiStatus : String = ""
    @State private var location : String = ""
    @StateObject var locadd = LocationAddressImpl()
    
    @available(iOS 15.0, *)
    var body: some View {
        if #available(iOS 15.0, *) {
            VStack (spacing: 30) {
                Text("Body Mass index calculotor")
                Text("\(locadd.location)")
                Text("Weight (KGs)")
                TextField("Weight", text: $weightKgs)
                    .textFieldStyle(.roundedBorder)
                Text("Height (CMs)")
                TextField("Height", text: $heightCMs)
                    .textFieldStyle(.roundedBorder)
                
                Button(
                    "Calculate",
                    action: {
                        //some code
                        bmiShow = true
                        
                        bmiStatus = BMIcalculator().calculateBMI(heightCMS: Float(heightCMs)!, weightKGS: Float(weightKgs)!)
                        
                        let profile = ProfileDetails(locationDetails: NativeLocationDetailsiOS()) // accessing interface type native impl
                        let locRes = NativeLocationDetailsiOS().getLocationAddress(locationAddress: locadd)
                        location = locadd.location
                        Logger().log("The address locadd is \(locadd.location)")
                        
                    }
                )
            }.padding(30)
                .alert(
                    "BMI Result is \(bmiStatus)",
                    isPresented: $bmiShow
                ) {
                    Button("Close", action: { bmiShow = false })
                }
        } else {
            // Fallback on earlier versions
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        if #available(iOS 15.0, *) {
            ContentView()
        } else {}
    }
}

class LocationAddressImpl : ObservableObject, LocationAddress {
    @Published var location = ""
    func onReceiveLocationAddress(address: String) -> String {
        Logger().log("The address is \(address)")
        location = address
        return address
    }
}

class NativeLocationDetailsiOS : NativeLocationDetails {
    
    func getLocationAddress(locationAddress: LocationAddress) {
        let locationManager = CLLocationManager()
               let currentLoc: CLLocation

               locationManager.requestWhenInUseAuthorization()
        
        if (CLLocationManager.authorizationStatus() == .authorizedAlways || CLLocationManager.authorizationStatus() == .authorizedWhenInUse)
                
            
        {
            currentLoc = locationManager.location!
                      

                       let clDecoder = CLGeocoder()
                
            clDecoder.reverseGeocodeLocation(currentLoc) { places, errors in
                if let res = places?.first {
                    let address = "\(res.name), \(res.locality), \(res.administrativeArea), \(res.country)"
                    //res.name + ", " + res.locality + ", " + res.administrativeArea + ", " + res.country
                    locationAddress.onReceiveLocationAddress(address: address)
                }

                
                
                
            }
        }

    }
    
    
}
