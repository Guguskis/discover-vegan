[string]$rootFolder = 'C:/Dev/bakalaurinis/discover-vegan'

[bool]$startDeployments = 1
[bool]$startServices = 1
[bool]$startIngress = 1
[bool]$startAdditionalResources = 1

class KubernetesResource
{
    [string]$module

    KubernetesResource([string]$module)
    {
        $this.module = $module
    }
}

$kubernetesResources = @(
[KubernetesResource]::new('search-service');
[KubernetesResource]::new('file-service');
[KubernetesResource]::new('authentication-service');
)

$ingressName = 'ingress'

Write-Output "----------------building docker images----------------------------------------------"
& minikube -p minikube docker-env | Invoke-Expression
$kubernetesResources | ForEach-Object {
docker build -t $_.module "$rootFolder/$($_.module)"
}
Write-Output "----------------docker images built   ----------------------------------------------"

If ($startAdditionalResources) {
Write-Output "----------------starting kubernetes additional resources------------------------"
kubectl apply -f "$rootFolder/additional-kubernetes-resources/mongodb-statefulset.yml"
kubectl apply -f "$rootFolder/additional-kubernetes-resources/mongodb-service.yml"
} else {
Write-Output "----------------kubernetes additional resourcse start was turned off------------"
}

If ($startServices) {
Write-Output "----------------starting kubernetes services------------------------------------"
$kubernetesResources | ForEach-Object {
kubectl apply -f "$rootFolder/$($_.module)/kubernetes-service.yml"
}
} else {
Write-Output "----------------kubernetes services start was turned off------------------------"
}

If ($startDeployments) {
Write-Output "----------------starting kubernetes deployments---------------------------------"
$kubernetesResources | ForEach-Object {
kubectl apply -f "$rootFolder/$($_.module)/kubernetes-deployment.yml"
}
} else {
Write-Output "----------------kubernetes deployments start was turned off---------------------"
}

If ($startIngress) {
Write-Output "----------------starting kubernetes ingress-------------------------------------"
kubectl apply -f "$rootFolder/$ingressName.yml"
} else {
Write-Output "----------------kubernetes ingress start was turned off-------------------------"
}
