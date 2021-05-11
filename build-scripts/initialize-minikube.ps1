minikube start --vm = true

Start-Process powershell.exe -ArgumentList "minikube mount C:/Dev/bakalaurinis/h2database:/h2database"

minikube addons enable ingress