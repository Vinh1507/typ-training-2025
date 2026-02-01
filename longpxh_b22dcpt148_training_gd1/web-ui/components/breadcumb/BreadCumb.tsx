"use client";

import * as React from 'react';
import Breadcrumbs from '@mui/material/Breadcrumbs';
import Link from '@mui/material/Link';


interface Inherit {
    labelInherit: string,
    hrefInherit: string
}

interface Primary {
    labelPrimary: string,
    hrefPrimary: string
}

interface BreadCumbProg {
    inherit: Array<Inherit>
    primary: Primary
    onClickBreadCumb?: (href: string) => void
}

export default function BreadCumb({ inherit, primary, onClickBreadCumb }: BreadCumbProg) {
    return (
        <div role="presentation">
            <Breadcrumbs aria-label="breadcrumb" className="!mt-5 !mb-7" >

                {/*inherit*/}

                {
                    inherit.map((item, index) => {
                        return (
                            <Link key={index} underline="hover"
                                color="inherit"
                                href={item.hrefInherit}
                                onClick={() => onClickBreadCumb?.(item.hrefInherit)}
                            >
                                {item.labelInherit}
                            </Link>
                        );
                    })
                }

                {/*primary*/}

                {
                    <Link
                        underline="hover"
                        color="text.primary"
                        href={primary.hrefPrimary}
                        aria-current="page"
                        onClick={() => onClickBreadCumb?.(primary.hrefPrimary)}

                    >
                        {primary.labelPrimary}
                    </Link>
                }
            </Breadcrumbs>
        </div>
    );
}
