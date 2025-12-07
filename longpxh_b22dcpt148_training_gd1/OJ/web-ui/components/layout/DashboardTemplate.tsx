"use client";

import * as React from 'react';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemText from '@mui/material/ListItemText';
import {Alert, Container, Link, Typography} from "@mui/material";
import {useRouter} from "next/navigation";

type Anchor = 'right';

interface ItemDrawer{
    label: string,
    href: string,
    onClickItem?: () => void
}

interface DashboardTemplateProg{
    itemDrawerList: Array<ItemDrawer>,
    namePage: string
}

export default function DashboardTemplate({itemDrawerList, namePage}:DashboardTemplateProg) {
    const [state, setState] = React.useState({
        right: false,
    });

    const router = useRouter()

    const toggleDrawer =
        (anchor: Anchor, open: boolean) =>
            (event: React.KeyboardEvent | React.MouseEvent) => {
                if (
                    event.type === 'keydown' &&
                    ((event as React.KeyboardEvent).key === 'Tab' ||
                        (event as React.KeyboardEvent).key === 'Shift')
                ) {
                    return;
                }

                setState({...state, [anchor]: open});
            };

    const list = (anchor: Anchor) => (
        <Box
            role="presentation"
            onClick={toggleDrawer(anchor, false)}
            onKeyDown={toggleDrawer(anchor, false)}
        >
            <List>
                {itemDrawerList.map((item, index) => (
                    <ListItem key={index} disablePadding>
                        <ListItemButton>
                            <ListItemText
                                onClick={()=> {
                                    router.push(item.href)
                                }}
                                primary={item.label}/>
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
        </Box>
    );

    return (

        <Container>
            <Alert severity="info" sx={{mb: 4}}>
                <Typography variant="h4" gutterBottom>
                    {namePage}
                </Typography>
                <Typography variant="body2">
                    Chào mừng quay trở lại, click để xem các chức năng!

                    <Typography component={Link} variant="body2">
                        {(['right'] as const).map((anchor) => (
                            <React.Fragment key={anchor}>
                                <Button onClick={toggleDrawer(anchor, true)}>Chức năng</Button>
                                <Drawer
                                    anchor={anchor}
                                    open={state[anchor]}
                                    onClose={toggleDrawer(anchor, false)}
                                >
                                    {list(anchor)}
                                </Drawer>
                            </React.Fragment>
                        ))}
                    </Typography>
                </Typography>
            </Alert>
        </Container>
    );
}