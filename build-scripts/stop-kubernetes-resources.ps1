[string]$rootFolder = 'C:/Dev/bakalaurinis/discover-vegan'

[bool]$stopDeployments = 1
[bool]$stopServices = 1
[bool]$stopIngress = 1

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
