minikube start --memory=8g --cpus=4 --vm=true

Start-Process powershell.exe -ArgumentList "minikube mount C:/Dev/bakalaurinis/h2database:/h2database"

minikube addons enable ingress