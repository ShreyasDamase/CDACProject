import React, { useEffect, useState } from 'react';
import axios from 'axios';

function ImageGallery() {
    const {session}=useClerk();
    return (
        <div>
            {imageUrls.length > 0 ? (
                imageUrls.map((url, index) => (
                    <div key={index}>
                        <img src={url} alt={`Image ${index + 1}`} />
                    </div>
                ))
            ) : (
                <p>No images found.</p>
            )}
        </div>
    );
}

export default ImageGallery;
