[string]$rootFolder = 'C:/Dev/bakalaurinis/discover-vegan'

[bool]$stopDeployments = 1
[bool]$stopServices = 1
[bool]$stopIngress = 1
[bool]$stopAdditionalResources = 1

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

$kubernetesResources | ForEach-Object {
If ($stopDeployments) {
kubectl delete -f "$rootFolder/$($_.module)/kubernetes-deployment.yml"
}
If ($stopServices) {
kubectl delete -f "$rootFolder/$($_.module)/kubernetes-service.yml"
}
}

If ($stopIngress) {
kubectl delete -f "$rootFolder/$ingressName.yml"
}

If ($stopAdditionalResources) {
Write-Output "----------------stoppping kubernetes additional resources------------------------"
kubectl delete -f "$rootFolder/additional-kubernetes-resources/mongodb-statefulset.yml"
kubectl delete -f "$rootFolder/additional-kubernetes-resources/mongodb-service.yml"
} else {
Write-Output "----------------kubernetes additional resourcse stop was turned off------------"
}