# Script to fix imports after DTO restructuring

$sourceRoot = "src\main\java\com\BTL_JAVA\BTL"

# Define mapping of old imports to new imports
$importMappings = @{
    # Request mappings - Auth
    'com.BTL_JAVA.BTL.DTO.Request.AuthenticationRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Auth.AuthenticationRequest'
    'com.BTL_JAVA.BTL.DTO.Request.IntrospectRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Auth.IntrospectRequest'
    'com.BTL_JAVA.BTL.DTO.Request.LogoutRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Auth.LogoutRequest'
    'com.BTL_JAVA.BTL.DTO.Request.RefreshRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Auth.RefreshRequest'
    
    # Request mappings - User
    'com.BTL_JAVA.BTL.DTO.Request.UserCreationRequest' = 'com.BTL_JAVA.BTL.DTO.Request.User.UserCreationRequest'
    'com.BTL_JAVA.BTL.DTO.Request.UserUpdateRequest' = 'com.BTL_JAVA.BTL.DTO.Request.User.UserUpdateRequest'
    'com.BTL_JAVA.BTL.DTO.Request.AddressRequest' = 'com.BTL_JAVA.BTL.DTO.Request.User.AddressRequest'
    
    # Request mappings - Product
    'com.BTL_JAVA.BTL.DTO.Request.ProductCreationRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.ProductCreationRequest'
    'com.BTL_JAVA.BTL.DTO.Request.ProductUpdateRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.ProductUpdateRequest'
    'com.BTL_JAVA.BTL.DTO.Request.CategoryCreationRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.CategoryCreationRequest'
    'com.BTL_JAVA.BTL.DTO.Request.CategoryUpdateRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.CategoryUpdateRequest'
    'com.BTL_JAVA.BTL.DTO.Request.ProductVariationCreationRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.ProductVariationCreationRequest'
    'com.BTL_JAVA.BTL.DTO.Request.ProductVariationUpdateRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.ProductVariationUpdateRequest'
    'com.BTL_JAVA.BTL.DTO.Request.CartItemRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.CartItemRequest'
    'com.BTL_JAVA.BTL.DTO.Request.UpdateQuantityRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Product.UpdateQuantityRequest'
    
    # Request mappings - Review
    'com.BTL_JAVA.BTL.DTO.Request.ReviewRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Review.ReviewRequest'
    'com.BTL_JAVA.BTL.DTO.Request.FeedbackRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Review.FeedbackRequest'
    
    # Request mappings - Sales
    'com.BTL_JAVA.BTL.DTO.Request.SalesCreationRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Sales.SalesCreationRequest'
    'com.BTL_JAVA.BTL.DTO.Request.SalesUpdateRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Sales.SalesUpdateRequest'
    'com.BTL_JAVA.BTL.DTO.Request.ProductSaleItemRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Sales.ProductSaleItemRequest'
    
    # Request mappings - Security
    'com.BTL_JAVA.BTL.DTO.Request.PermissionRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Security.PermissionRequest'
    'com.BTL_JAVA.BTL.DTO.Request.RoleRequest' = 'com.BTL_JAVA.BTL.DTO.Request.Security.RoleRequest'
    
    # Response mappings - Auth
    'com.BTL_JAVA.BTL.DTO.Response.AuthenticationResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Auth.AuthenticationResponse'
    'com.BTL_JAVA.BTL.DTO.Response.IntrospectResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Auth.IntrospectResponse'
    
    # Response mappings - User
    'com.BTL_JAVA.BTL.DTO.Response.UserResponse' = 'com.BTL_JAVA.BTL.DTO.Response.User.UserResponse'
    'com.BTL_JAVA.BTL.DTO.Response.AddressResponse' = 'com.BTL_JAVA.BTL.DTO.Response.User.AddressResponse'
    
    # Response mappings - Product
    'com.BTL_JAVA.BTL.DTO.Response.ProductResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Product.ProductResponse'
    'com.BTL_JAVA.BTL.DTO.Response.ProductVariationResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Product.ProductVariationResponse'
    'com.BTL_JAVA.BTL.DTO.Response.CategoryResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Product.CategoryResponse'
    'com.BTL_JAVA.BTL.DTO.Response.CartItemResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Product.CartItemResponse'
    'com.BTL_JAVA.BTL.DTO.Response.ProductSaleItemResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Product.ProductSaleItemResponse'
    
    # Response mappings - Review
    'com.BTL_JAVA.BTL.DTO.Response.ReviewResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Review.ReviewResponse'
    'com.BTL_JAVA.BTL.DTO.Response.FeedbackResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Review.FeedbackResponse'
    'com.BTL_JAVA.BTL.DTO.Response.UserReviewsResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Review.UserReviewsResponse'
    'com.BTL_JAVA.BTL.DTO.Response.ProductFeedbackSummary' = 'com.BTL_JAVA.BTL.DTO.Response.Review.ProductFeedbackSummary'
    
    # Response mappings - Sales
    'com.BTL_JAVA.BTL.DTO.Response.SalesResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Sales.SalesResponse'
    
    # Response mappings - Security
    'com.BTL_JAVA.BTL.DTO.Response.RoleResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Security.RoleResponse'
    'com.BTL_JAVA.BTL.DTO.Response.PermissionResponse' = 'com.BTL_JAVA.BTL.DTO.Response.Security.PermissionResponse'
}

# Get all Java files except DTO folder (already updated)
$javaFiles = Get-ChildItem -Path $sourceRoot -Recurse -Filter "*.java" | 
    Where-Object { $_.FullName -notmatch "\\DTO\\Request\\" -and $_.FullName -notmatch "\\DTO\\Response\\" }

$totalFiles = $javaFiles.Count
$processedFiles = 0
$modifiedFiles = 0

Write-Host "Found $totalFiles Java files to process..."

foreach ($file in $javaFiles) {
    $processedFiles++
    $content = Get-Content $file.FullName -Raw -Encoding UTF8
    $originalContent = $content
    $modified = $false
    
    # Replace each old import with new import
    foreach ($oldImport in $importMappings.Keys) {
        $newImport = $importMappings[$oldImport]
        if ($content -match [regex]::Escape($oldImport)) {
            $content = $content -replace [regex]::Escape($oldImport), $newImport
            $modified = $true
        }
    }
    
    # Save if modified
    if ($modified -and $content -ne $originalContent) {
        Set-Content -Path $file.FullName -Value $content -Encoding UTF8 -NoNewline
        $modifiedFiles++
        Write-Host "[$processedFiles/$totalFiles] Updated: $($file.Name)"
    }
}

Write-Host "`nDone! Modified $modifiedFiles out of $totalFiles files."

