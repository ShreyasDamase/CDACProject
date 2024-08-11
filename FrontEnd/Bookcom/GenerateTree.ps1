function Generate-FilteredTree {
    param (
        [string]$Path
    )
    
    # Create an empty array to hold paths
    $output = @()

    # Define the directories to include explicitly
    $includeDirs = @("public", "src", "src\components", "src\util")

    # Get all directories recursively, excluding 'node_modules'
    $items = Get-ChildItem -Path $Path -Recurse -Force | Where-Object { $_.FullName -notmatch '\\node_modules\\' }

    foreach ($item in $items) {
        $relativePath = $item.FullName.Substring($Path.Length).TrimStart('\')
        
        # Check if the directory or file is within the include list
        if ($includeDirs -contains ($relativePath.Split('\')[0])) {
            $output += $relativePath
        }
    }

    # Output to file
    $output | Out-File -FilePath "$Path\tree.txt" -Encoding UTF8
}

# Define the path
$path = "D:\Webprograming\CDAC project\FrontEnd\Bookcom"

# Generate the filtered tree
Generate-FilteredTree -Path $path
